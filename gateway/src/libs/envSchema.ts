import Joi from 'joi'
  
export const envSchema = Joi.object({
    NODE_ENV: Joi.string().allow('development', 'production').required(),
    PORT: Joi.number().min(1025).max(65535).required(),
    // logging
    LOGS_LEVEL: Joi.string().allow('error', 'warn', 'info', 'verbose', 'debug', 'silly').default('info'),
    FIREBASE_API_KEY: Joi.string().required(),
    FIREBASE_AUTH_DOMAIN: Joi.string().required(),
    FIREBASE_PROJECT_ID: Joi.string().required(),
    FIREBASE_STORAGE_BUCKET: Joi.string().required(),
    FIREBASE_MESSAGING_SENDER_ID: Joi.number().required(),
    FIREBASE_APP_ID: Joi.string().required(),
    FIREBASE_MEASUREMENET_ID: Joi.string().required(),
}).required();