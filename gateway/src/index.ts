import dotenv from 'dotenv'
import { validateEnv } from './libs/envSchema'
import fs, { Stats } from 'fs';
import util from 'util';
const stat = util.promisify(fs.stat);

const result = dotenv.config()
const response: boolean = validateEnv(result.parsed)
if (response !== true) {
    process.exit(1)
}

import { bootstrapServer } from './libs/server/bootstrapServer'

(async () => {
    let statObj: Stats
    try {
        statObj = await stat('./serviceConf.json');
    } catch (e) {
        console.error("Can't find serviceConf.json file. It's required for Firebase SDK!");
        process.exit(1);
    }

    if (statObj.isFile()) {
        bootstrapServer()
            .catch(e => console.error(`Cannot bootstrap server: ${e.message}`))
    } else {
        console.error("The serviceConf.json doesn't seem to be a file!");
    }
})();
