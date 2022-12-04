import BaseModel from "./BaseModel.js";


export default class Band extends BaseModel {

    public id?: number;
    public name?: string;
    public country?: string;
    public country_name?: string;
    public active_from?: number;
    public active_to?: number;
    public total_albums_released?: number;
    public website?: string;
    public twitter?: string;
    public youtube?: string;
    public musicbrainz_url?: string
    
    public tableName(): string {
        return 'bands';
    }

    async save(): Promise<number> {
        if (this.dryRun) {
            return 1
        }

        if (!this.name) {
           throw new Error('Band: name cannot be null')
        } 
      
        const sql = `
            INSERT INTO ${this.tableName()} (name, country, country_name, active_from,
                active_to, total_albums_released, website, twitter, youtube, musicbrainz_url
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        `;

        const values = [this.name, this.country, this.country_name, this.active_from, 
            this.active_to, this.total_albums_released, this.website, this.twitter, this.youtube,
            this.musicbrainz_url
        ];

        const results = await this.query(sql, values)
        this.id = results.insertId;
        return this.id!;
    }
}