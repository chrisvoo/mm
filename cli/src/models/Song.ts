import BaseModel from "./BaseModel.js";


export default class Song extends BaseModel {

    public id?: number;
    public title?: string;
    public album_id?: number;
    
    public tableName(): string {
        return 'songs';
    }

    async save(): Promise<number> {
        if (this.dryRun) {
            return 1
        }
        
        if (!this.title || !this.album_id) {
           throw new Error('Song: title and album_id cannot be null')
        } 
      
        const sql = `
            INSERT INTO ${this.tableName()} (title, album_id) VALUES (?, ?)
        `;

        const values = [this.title, this.album_id];

        const results = await this.query(sql, values)
        this.id = results.insertId;
        return this.id!;
    }
}