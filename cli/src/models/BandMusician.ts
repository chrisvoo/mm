import BaseModel from "./BaseModel.js";

export default class BandMusician extends BaseModel {
    
    public band_id?: number;
    public musician_id?: number;
    
    public tableName(): string {
        return 'band_musicians';
    }

    async save(): Promise<number> {
        if (this.dryRun) {
            return 1
        }

        if (!this.band_id || !this.musician_id) {
            throw new Error('Bandusician: band_id and musician_id cannot be null')
        }

        const sql = `
            INSERT INTO ${this.tableName()} (band_id, musician_id) VALUES (?, ?)
        `;

        const values = [this.band_id, this.musician_id];

        const results = await this.query(sql, values)
        return 1;
    }
}