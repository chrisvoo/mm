import { FastifyInstance, FastifyRequest, FastifyReply } from "fastify"
import { auth } from "./auth/firebase"
import { Static, Type } from '@sinclair/typebox'
import { signInWithEmailAndPassword, signOut } from "firebase/auth"

/**
 * Encapsulates the routes
 * @param { FastifyInstance } fastify  Encapsulated Fastify Instance
 * @param { Object } options plugin options, refer to https://www.fastify.io/docs/latest/Reference/Plugins/#plugin-options
 */
export async function routes (fastify: FastifyInstance, options: any) {
    fastify.get('/signout', async (request: FastifyRequest, reply: FastifyReply) => {
        return signOut(auth);
    });

    fastify.get('/logged-in', async (request: FastifyRequest, reply: FastifyReply) => {
        return { logged_in: auth.currentUser !== null }
    });
}

