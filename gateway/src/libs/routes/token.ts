import { Type, Static } from "@sinclair/typebox";
import { FastifyInstance, FastifyReply, FastifyRequest } from "fastify"
import { FirebaseError } from "firebase/app";
import { signInWithEmailAndPassword } from "firebase/auth";
import { auth } from "../auth/firebase";

/**
 * Encapsulates the route for obtaining a valid token
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

    fastify.post('/token', { schema: { body: bodyJsonSchema } }, async (request: FastifyRequest, reply: FastifyReply) => {
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

    // fastify.get('/logged-in', async (request: FastifyRequest, reply: FastifyReply) => {
    //     return adminAuth().verifyIdToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IjA2M2E3Y2E0M2MzYzc2MDM2NzRlZGE0YmU5NzcyNWI3M2QwZGMwMWYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbXVzaWMtbWFuYWdlci00NDA0ZSIsImF1ZCI6Im11c2ljLW1hbmFnZXItNDQwNGUiLCJhdXRoX3RpbWUiOjE2NjAwODU3OTEsInVzZXJfaWQiOiJwTjlQZ0VJR1hDZGJlcWlUY0pFVWZLekJjWDczIiwic3ViIjoicE45UGdFSUdYQ2RiZXFpVGNKRVVmS3pCY1g3MyIsImlhdCI6MTY2MDA4NTc5MSwiZXhwIjoxNjYwMDg5MzkxLCJlbWFpbCI6InRlc3RfdXNlckBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsidGVzdF91c2VyQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.S7vqM6FheHnt6h42bP8F4FBp_aYD3kjk54hPpr3WhjPxBRpZlMCB1fL1-qSrsJMKvNIBbycrP18psnoga8zoUPz1TRaUwQuKvMPkM4lh4ywZ9G0ovAi14vK3eZzmEy6TzC6WRP-5vOu1EX-TmKdLz4fvw8dePSJyK6tCsg7Dyzm9YUgMSowgG2eB_od0Yhye-7cEMjyczgyPbYlXudb0MzP6XibEWuJgWklZIW68KM3yvwdJ2mmi2OsjP4bo14r5AijduJHGBcRO1WER_nwuDnElKxfDiQgn8K7WoyiY5NMDH58kdFFQX9A11Z2a9NBQZ1JtMRHUYBUBUzKNKR5tdg", true)
    // });
}