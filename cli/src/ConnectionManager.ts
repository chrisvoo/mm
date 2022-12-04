import mysql, { Connection } from 'mysql2/promise';

export default class ConnectionManager {
    private static connection?: Connection;

    static isEnvValid(): boolean {
        return (
            process.env.MYSQL_USER !== undefined &&
            process.env.MYSQL_PASSWORD !== undefined &&
            process.env.MYSQL_DATABASE !== undefined
        );
    }

    static async connect(): Promise<Connection> {
        if (ConnectionManager.connection) {
            return ConnectionManager.connection
        }

        if (!ConnectionManager.isEnvValid()) {
            throw new Error('Missing required ENV variables for connection')
        }

        ConnectionManager.connection = await mysql.createConnection({
            host     : 'localhost',
            user     : process.env.MYSQL_USER,
            password : process.env.MYSQL_PASSWORD,
            database : process.env.MYSQL_DATABASE,
            connectTimeout: 3000,
            supportBigNumbers: true,
            decimalNumbers: true,
            namedPlaceholders: true
        });

        ConnectionManager.connection.connect();

        return ConnectionManager.connection;
    }

    static disconnect(): Promise<void> {
        if (ConnectionManager.connection) {
            // makes sure all remaining queries are executed before sending a quit packet to the mysql server.
            return ConnectionManager.connection.end();
        }

        return Promise.resolve()
    }
}