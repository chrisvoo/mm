import { bootstrapServer } from './libs/server/bootstrapServer'

bootstrapServer()
    .catch(e => console.error(`Cannot bootstrap server: ${e.message}`))
