import { Connection } from "mysql2/promise";
import ConnectionManager from "../ConnectionManager.js";

export default abstract class BaseModel {
    public dryRun: boolean = false

    /**
     * Returns the table name
     */
    public abstract tableName(): string;

    /**
     * Returns the last inserted id (not always needed)
     */
    public abstract save(): Promise<number>;

    /**
     * Uses a singleton class to retrieve the connection
     * @returns The connection
     */
    protected getConnection(): Promise<Connection>
    {
        return ConnectionManager.connect()
    }

    /**
     * Makes a potentially invalid date into a valid one
     * @param date The date to be saved
     * @returns the possibly modified date or null if it's an unrecognized format
     */
    protected safeDate(date?: string): string|null {
        if(!date) {
            return null;
        }

        if (/^\d{4}-\d{2}-\d{2}$/.test(date)) {
            return date
        }

        if (/^\d{4}-\d{2}$/.test(date)) {
            return `${date}-01`
        }

        if (/^\d{4}$/.test(date)) {
            return `${date}-01-01`
        }

        return null
    }

    /**
     * Performs a query on the database
     * @param sql The SQL statement
     * @returns The results (should be an array)
     */
    protected async query(sql: string, values: Array<any> = [], timeout: number = 10000): Promise<any>
    {
        const conn = await this.getConnection()

        const [rows] = await conn.execute({
            sql,
            values: values.map(i => i === undefined ? null : i),
            timeout,
        })

        return rows;
    }

    public async startTransaction(): Promise<void> {
        if (this.dryRun) {
            return
        }

        await this.getConnection()
            .then((conn) => conn.query('START TRANSACTION'));
    }

    public async commit(): Promise<void> {
        if (this.dryRun) {
            return
        }

        await this.getConnection()
            .then((conn) => conn.query('COMMIT'));
    }

    public async rollback(): Promise<void> {
        if (this.dryRun) {
            return
        }
        
        await this.getConnection()
            .then((conn) => conn.query('ROLLBACK'));
    }
}