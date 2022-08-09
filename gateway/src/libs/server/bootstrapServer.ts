import { FastifyInstance } from 'fastify';
import dotenv from 'dotenv'
import { validateEnv } from '../envSchema';
import fs, { Stats } from 'fs';
import util from 'util';
import fastify from './serverSetup'

export const VERSION = '0.0.1'
const stat = util.promisify(fs.stat);

export const bootstrapServer = async (): Promise<FastifyInstance> => {
    try {
        const result = dotenv.config()
        const response: boolean = validateEnv(result.parsed)
        if (response !== true) {
            process.exit(1)
        }

        let statObj: Stats
        try {
            statObj = await stat('./serviceConf.json');
        } catch (e) {
            console.error("Can't find serviceConf.json file. It's required for Firebase SDK!");
            process.exit(1);
        }

        if (!statObj.isFile()) {
            console.error("The serviceConf.json doesn't seem to be a file!");
            process.exit(1);
        }

        await fastify.listen({ 
            port: process.env.PORT as unknown as number 
        });
        return fastify;
    } catch (err) {
        fastify.log.error(err);
        process.exit(1);
    }
};