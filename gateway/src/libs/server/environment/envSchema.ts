import { Type }   from '@sinclair/typebox'
import Ajv, { DefinedError } from 'ajv'
import { DotenvParseOutput } from 'dotenv';

enum Environments {
    development = "development", 
    production = "production",
    test = "test"
}

enum LoggerLevels {
    error = "error", warn = "warn", info = "info", 
    verbose = "verbose", debug = "debug", silly = "debug"
}
  
const envSchema = Type.Required(
    Type.Object({
        NODE_ENV: Type.Enum(Environments),
        PORT: Type.Integer({ maximum: 65535, minimum: 1025 }),
        FILEMANAGER_URL: Type.String(),
        // logging
        LOGS_LEVEL: Type.Enum(LoggerLevels, { default: 'info' }),
        // Firebase
        GOOGLE_APPLICATION_CREDENTIALS: Type.String(),
        FIREBASE_API_KEY: Type.String(),
        FIREBASE_AUTH_DOMAIN: Type.String(),
        FIREBASE_PROJECT_ID: Type.String(),
        FIREBASE_STORAGE_BUCKET: Type.String(),
        FIREBASE_MESSAGING_SENDER_ID: Type.Number(),
        FIREBASE_APP_ID: Type.String(),
        FIREBASE_MEASUREMENET_ID: Type.String(),
    })
);

/**
 * It validates the content of .env file.
 * @param dotEnv The output derived from dotenv.config()
 * @returns true if the .env is valid, false otherwise
 */
export function validateEnv(dotEnv: DotenvParseOutput | undefined): boolean {
    const validate = new Ajv({
        allErrors: false,
        coerceTypes: true,
    }).compile(envSchema)

    if (!validate(dotEnv)) {
        for (const err of validate.errors as DefinedError[]) {
            switch (err.keyword) {
                case "type":
                    console.error(`${err.instancePath}: ${err.message}`);
                    return false;
            }
        }
    }

    return true;
}