import fastify from 'fastify'
import { routes } from './routes';

const server = fastify({
    logger: { 
        level: process.env.LOGS_LEVEL,
        prettyPrint: {
          colorize: true,
          ignore: 'pid,hostname',
          translateTime: 'SYS:yyyy-MM-dd HH:mm:ss p Z',
        },
    },
    caseSensitive: true,
})

server.register(routes);

export default server;