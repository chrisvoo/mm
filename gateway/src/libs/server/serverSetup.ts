import fastify from 'fastify'
import status from '../routes/status';
import token from '../routes/token';
import adminRoutes from '../routes/adminRoutes';

const server = fastify({
    logger: process.env.NODE_ENV === 'development' ? { 
        level: process.env.LOGS_LEVEL,
        prettyPrint: {
          colorize: true,
          ignore: 'pid,hostname',
          translateTime: 'SYS:yyyy-MM-dd HH:mm:ss p Z',
        },
    } : false,
    caseSensitive: true,
})

server
  .register(status)
  .register(token)
  .register(adminRoutes, { adminCheck: true });


export default server;