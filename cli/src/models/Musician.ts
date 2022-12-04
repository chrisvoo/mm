import BaseModel from "./BaseModel.js";

export default class Musician extends BaseModel {

    public id?: number;
    public real_name?: string;
    public artistic_name?: string;
    public instruments?: string;
    public birthday?: string;
    public died?: string;
    public musicbrainz_url?: string
    
    public tableName(): string {
        return 'musicians';
    }

    public async save(): Promise<number> {
        if (this.dryRun) {
            return 1
        }
        
        const sql = `
            INSERT INTO ${this.tableName()} (real_name, artistic_name, 
                instruments, birthday, died, musicbrainz_url)
            VALUES (?, ?, ?, ?, ?, ?)
        `;

        const values = [
            this.real_name, this.artistic_name, this.instruments, this.safeDate(this.birthday), 
            this.safeDate(this.died), this.musicbrainz_url
        ];

        const results = await this.query(sql, values)
        this.id = results.insertId;
        return this.id!;
    }
    
}