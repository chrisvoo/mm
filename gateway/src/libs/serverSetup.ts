import fastify from 'fastify'

const server = fastify({
    logger: { 
        level: process.env.LOGS_LEVEL,
        prettyPrint: process.env.NODE_ENV === 'development'
    },
    caseSensitive: true,
})

server.get('/ping', async (request, reply) => {
  return 'pong\n'
})

export default server;