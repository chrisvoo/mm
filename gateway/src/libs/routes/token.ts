import { Type } from "@sinclair/typebox";
import { FirebaseError } from "firebase/app";
import { signInWithEmailAndPassword, UserCredential } from "firebase/auth";
import { auth } from "../auth/firebase";
import { FastifyTypeBoxed } from "../types";

/**
 * Encapsulates the route for obtaining a valid token
 * @param { FastifyInstance } fastify  Encapsulated Fastify Instance
 * @param { Object } options plugin options, refer to https://www.fastify.io/docs/latest/Reference/Plugins/#plugin-options
 */
export default async function routes (fastify: FastifyTypeBoxed) {
    const bodyJsonSchema = {
            body: Type.Required(
            Type.Object({
                email: Type.String({ format: 'email' }),
                password: Type.String({ minLength: 8 })
            })
        )
    };

    fastify.post('/token', { schema: bodyJsonSchema }, async (request) => {
        try {
            const { email, password } = request.body;
            const response: UserCredential = await signInWithEmailAndPassword(auth, email, password)

            const { user } = response;
            const { token, expirationTime } = await user.getIdTokenResult()

            return {
                uid: user.uid,
                email: user.email,
                accessToken: token,
                expirationTime,
            }

        } catch (e) {
            const error = e as FirebaseError;
            return {
                error: 'Cannot get the access token',
                message: error.message,
                code: error.code
            }
        }
    });
}