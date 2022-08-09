import fastify from 'fastify'
import status from '../routes/status';
import signin from '../routes/signin';

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

server
  .register(status)
  .register(signin);


export default server;