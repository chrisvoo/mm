import BaseModel from "./BaseModel.js";

export default class Album extends BaseModel {
    
    public id?: number;
    public title?: string;
    public band_id?: number;
    public released_on?: string;

    // non-persisted properties
    public wikiPage?: string
    
    public tableName(): string {
        return 'albums';
    }

    async save(): Promise<number> {
        if (this.dryRun) {
            return 1
        }
        
        if (!this.title || !this.band_id) {
            throw new Error('Album: title and band_it cannot be null')
        }

        const sql = `
            INSERT INTO ${this.tableName()} (title, band_id, released_on) VALUES (?, ?, ?)
        `;

        const values = [this.title, this.band_id, this.released_on];

        const results = await this.query(sql, values)
        this.id = results.insertId;
        return this.id!;
    }
}