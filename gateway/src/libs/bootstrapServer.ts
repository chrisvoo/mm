import mercurius from 'mercurius'
import fastify from './serverSetup'

export const bootstrapServer = async (): Promise<void> => {
    try {
        await fastify.listen({ 
            port: process.env.PORT as unknown as number 
        });
    } catch (err) {
        fastify.log.error(err);
        process.exit(1);
    }
};