import BaseModel from "./BaseModel.js";

export default class AlbumMusician extends BaseModel {
    
    public album_id?: number;
    public musician_id?: number;
    
    public tableName(): string {
        return 'albums_musicians';
    }

    async save(): Promise<number> {
        if (this.dryRun) {
            return 1
        }
        
        if (!this.album_id || !this.musician_id) {
            throw new Error('AlbumMusician: album_id and musician_id cannot be null')
        }

        const sql = `
            INSERT INTO ${this.tableName()} (album_id, musician_id) VALUES (?, ?)
        `;

        const values = [this.album_id, this.musician_id];

        const results = await this.query(sql, values)
        return 1;
    }
}