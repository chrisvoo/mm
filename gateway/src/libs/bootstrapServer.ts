import { FastifyInstance } from 'fastify';
import fastify from './serverSetup'

export const VERSION = '0.0.1'

export const bootstrapServer = async (): Promise<FastifyInstance> => {
    try {
        await fastify.listen({ 
            port: process.env.PORT as unknown as number 
        });
        return fastify;
    } catch (err) {
        fastify.log.error(err);
        process.exit(1);
    }
};