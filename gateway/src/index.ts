import dotenv from 'dotenv'
import { envSchema } from './libs/envSchema'
import { bootstrapServer } from './libs/bootstrapServer'

const result = dotenv.config()
const { error } = envSchema.validate(result.parsed)
if (error) {
    console.error(error.message)
    process.exit(1)
}

bootstrapServer()
 .catch(e => console.error(`Cannot bootstrap server: ${e.message}`))