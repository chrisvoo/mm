import { FastifyInstance, FastifyReply, FastifyRequest } from "fastify"
import { VERSION } from "../server/bootstrapServer"

/**
 * Encapsulates the routes
 * @param { FastifyInstance } fastify  Encapsulated Fastify Instance
 * @param { Object } options plugin options, refer to https://www.fastify.io/docs/latest/Reference/Plugins/#plugin-options
 */
 export default async function routes (fastify: FastifyInstance, options: any) {
    fastify.get('/status', async (request: FastifyRequest, reply: FastifyReply) => {
        return { version: VERSION }
    })
}