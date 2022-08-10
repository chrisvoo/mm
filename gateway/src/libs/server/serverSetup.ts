import fastify from 'fastify'
import { TypeBoxTypeProvider } from '@fastify/type-provider-typebox'
import status from '../routes/status'
import token from '../routes/token'
import adminUserRoutes from '../routes/adminUserRoutes'
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

  return server;  
}
