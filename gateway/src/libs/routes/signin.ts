import { Type, Static } from "@sinclair/typebox";
import { FastifyInstance, FastifyReply, FastifyRequest } from "fastify"
import { FirebaseError } from "firebase/app";
import { signInWithEmailAndPassword } from "firebase/auth";
import { auth } from "../auth/firebase";

/**
 * Encapsulates the routes
 * @param { FastifyInstance } fastify  Encapsulated Fastify Instance
 * @param { Object } options plugin options, refer to https://www.fastify.io/docs/latest/Reference/Plugins/#plugin-options
 */
 export default async function routes (fastify: FastifyInstance, options: any) {
    const bodyJsonSchema = Type.Required(
        Type.Object({
            email: Type.String({ format: 'email' }),
            password: Type.String({ minLength: 8 })
        })
    );

    fastify.post('/signin', { schema: { body: bodyJsonSchema } }, async (request: FastifyRequest, reply: FastifyReply) => {
        try {
            const { email, password } = request.body as Static<typeof bodyJsonSchema>;
            return signInWithEmailAndPassword(auth, email, password)
        } catch (e) {
            const error = e as FirebaseError;
            return {
                error: 'Cannot sign-in',
                message: error.message,
                code: error.code
            }
        }
    });

    fastify.get('/logged-in', async (request: FastifyRequest, reply: FastifyReply) => {
        return { logged_in: auth.currentUser !== null }
    });
}