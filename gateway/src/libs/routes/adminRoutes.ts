import { FastifyInstance, FastifyReply, FastifyRequest, HookHandlerDoneFunction } from "fastify"
import injectVerifyBearerHook, { FastifyUserRequest } from './hooks/injectVerifyBearerHook';

/**
 * Encapsulates the route for obtaining a valid token
 * @param { FastifyInstance } fastify  Encapsulated Fastify Instance
 * @param { Object } options plugin options, refer to https://www.fastify.io/docs/latest/Reference/Plugins/#plugin-options
 */
export default async function routes (fastify: FastifyInstance, options: any) {
    injectVerifyBearerHook(fastify)

    // fastify.get('/ciaone', async (request: FastifyRequest, reply: FastifyReply) => {
    //     return 'ciao ' + (request as FastifyUserRequest).user.email ;   
    // })
}