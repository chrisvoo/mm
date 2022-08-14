import fastify, { FastifyReply, FastifyRequest } from 'fastify'
import { TypeBoxTypeProvider } from '@fastify/type-provider-typebox'
import proxy from '@fastify/http-proxy';
import status from '../routes/status'
import token from '../routes/token'
import adminUserRoutes from '../routes/adminUserRoutes'
import injectVerifyBearerHook, { FastifyUserRequest, verifyBearerToken } from '../routes/hooks/injectVerifyBearerHook';
import { FastifyTypeBoxed, ServerSetupOptions } from '../types'


export default function serverSetup(opt?: ServerSetupOptions): FastifyTypeBoxed {
  const server = fastify({
      logger: !opt?.skipLogging ? { 
          level: process.env.LOGS_LEVEL,
          transport: {
            target: 'pino-pretty',
            options: {
              colorize: true,
              ignore: 'pid,hostname',
              translateTime: 'SYS:yyyy-MM-dd HH:mm:ss p Z',
            }
          }
      }: false,
      caseSensitive: true,
  }).withTypeProvider<TypeBoxTypeProvider>()

  server
    .register(status)
    .register(token)
    .register(adminUserRoutes, { adminCheck: true, prefix: '/user' })
    .register(proxy, {
      upstream: process.env.FILEMANAGER_URL!, // An URL (including protocol) that represents the target server to use for proxying.
      prefix: '/fm', // All the requests to the current server starting with the given prefix will be proxied to the provided upstream. 
      http2: false, // optional
      logLevel: 'info',
      preHandler: (
        request: FastifyRequest,
        reply: FastifyReply
      ) => {
        return verifyBearerToken(request, reply)
      }
    })
    

  return server;  
}
