import { expect } from 'chai';
import '../../src/libs/server/environment/loadEnvVars';
import { bootstrapServer, VERSION } from '../../src/libs/server/bootstrapServer'
import { FastifyTypeBoxed } from '../../src/libs/types';

let fastifyApp: FastifyTypeBoxed|null = null;

describe('/status', function () {
    before(async function () {
        fastifyApp = await bootstrapServer({ skipLogging: true });
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
