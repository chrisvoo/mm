import { FastifyReply, FastifyRequest } from "fastify"
import { auth as adminAuth } from "firebase-admin"
import { FirebaseError } from "firebase/app";
import { FastifyTypeBoxed, VerifyBearerTokenOptions } from "../../types";

export interface FastifyUserRequest extends FastifyRequest {
    user: {
        uid: string
        email: string
    }
}

export async function verifyBearerToken(request: FastifyRequest, reply: FastifyReply, options?: VerifyBearerTokenOptions): Promise<FastifyReply | void> {
    const { authorization } = request.headers;
    if (!authorization) {
        reply.status(403).send({
            error: 'Authorization required'
        })
        return reply;
    }

    const bearerType = 'Bearer';
    const key = authorization.substring(bearerType.length).trim();
    try {
        const response: adminAuth.DecodedIdToken = await adminAuth().verifyIdToken(key);
        if (options?.adminCheck === true && response.email !== 'voodoo81people@gmail.com') {
            reply.status(403).send({
                error: 'Authorization required',
                code: 'admin-route'
            })
            return reply;
        }

        const { uid, email } = response;

        (request as FastifyUserRequest).user = {
            uid, email: email ?? ''
        }
    } catch (e) {
        const error = e as FirebaseError;
        reply.status(403).send({
            error: 'Authorization required',
            code: error.code,
            message: error.message
        })
        return reply
    }
}

export default function injectVerifyBearerHook(fastify: FastifyTypeBoxed, options?: VerifyBearerTokenOptions): FastifyReply | void {
    fastify.decorateRequest('user', null);

    fastify.addHook('onRequest', async (request: FastifyRequest, reply: FastifyReply) => {
        verifyBearerToken(request, reply, options)
    })
}