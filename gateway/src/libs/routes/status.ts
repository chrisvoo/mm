import { VERSION } from "../server/bootstrapServer"
import { FastifyTypeBoxed } from "../types"

/**
 * Encapsulates the routes
 * @param { FastifyInstance } fastify  Encapsulated Fastify Instance
 * @param { Object } options plugin options, refer to https://www.fastify.io/docs/latest/Reference/Plugins/#plugin-options
 */
export default async function routes (fastify: FastifyTypeBoxed) {
    fastify.get('/status', async () => {
        return { version: VERSION }
    })
}