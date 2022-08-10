import { Type } from "@sinclair/typebox";
import { auth as adminAuth } from "firebase-admin"
import { FastifyTypeBoxed } from "../types";
import injectVerifyBearerHook from './hooks/injectVerifyBearerHook';

/**
 * Encapsulates the route for obtaining a valid token
 * @param { FastifyInstance } fastify  Encapsulated Fastify Instance
 * @param { Object } options plugin options, refer to https://www.fastify.io/docs/latest/Reference/Plugins/#plugin-options
 */
export default async function routes (fastify: FastifyTypeBoxed, options: any) {
    injectVerifyBearerHook(fastify, options)

    const UserParamJsonSchema = Type.Required(
        Type.Object({
            uid: Type.String()
        })
    );

    const UserBodyJsonSchema = Type.Object({
        email: Type.String(),
        emailVerified: Type.Optional(Type.Boolean({ default: true })),
        password: Type.String({ minLength: 8 }),
        displayName: Type.Optional(Type.String()),
        photoURL: Type.Optional(Type.String()),
        disabled: Type.Optional(Type.Boolean({ default: false })),
    });

    // single user by uid
    fastify.get('/:uid', { schema: { params: UserParamJsonSchema } }, async (request,) => {
        const { uid } = request.params;
        const user = await adminAuth().getUser(uid);
        return user.toJSON()
    })

    // user creation
    fastify.put('/:uid', { schema: { body: UserBodyJsonSchema } }, async (request) => {
        const { email, emailVerified = true, password, displayName, photoURL, disabled = false } = request.body
        // Firebase Authentication will generate a random uid for the new user
        const user = await adminAuth().createUser({
            email,
            disabled,
            displayName,
            emailVerified,
            photoURL,
            password
        })

        return {
            success: true,
            uid: user.uid
        }
    })

    // user update
    fastify.post('/:uid', { schema: { body: UserBodyJsonSchema, params: UserParamJsonSchema } }, async (request) => {
        const { email, emailVerified = true, password, displayName, photoURL, disabled = false } = request.body
        const { uid } = request.params;

        const user = await adminAuth().updateUser(uid, {
            email,
            disabled,
            displayName,
            emailVerified,
            photoURL,
            password
        })
        
        return {
            success: true,
            userUpdated: user.toJSON()
        }
    })

    // delete user
    fastify.delete('/:uid', { schema: { params: UserParamJsonSchema } }, async (request) => {
        const { uid } = request.params;

        await adminAuth().deleteUser(uid);

        return {
            success: true
        }
    });
}