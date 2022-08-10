import { expect } from 'chai';
import { FastifyInstance } from 'fastify';
import '../../src/libs/server/environment/loadEnvVars';
import { bootstrapServer, VERSION } from '../../src/libs/server/bootstrapServer'

let fastifyApp: FastifyInstance|null = null;

describe('/status', function () {
    before(async function () {
        fastifyApp = await bootstrapServer();
    });

    after(function () {
        fastifyApp?.close();
    })

    it('can receive the status', async () => {
        const response = await fastifyApp?.inject({
            method: 'GET',
            url: '/status'
        })
        expect(response?.json().version).to.equal(VERSION);
    });
});
