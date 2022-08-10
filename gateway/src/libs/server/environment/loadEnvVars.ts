import dotenv from 'dotenv'
import { validateEnv } from './envSchema';

const result = dotenv.config();
const response: boolean = validateEnv(result.parsed)
if (response !== true) {
    process.exit(1)
}