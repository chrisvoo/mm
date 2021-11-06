import Joi from 'joi'
  
export const envSchema = Joi.object({
    NODE_ENV: Joi.string().allow('development', 'production').required(),
    PORT: Joi.number().min(1025).max(65535).required(),
    // logging
    LOGS_LEVEL: Joi.string().allow('error', 'warn', 'info', 'verbose', 'debug', 'silly').default('info'),
    LOGS_DAYS_RETENTION: Joi.number().integer().min(1),
}).required();