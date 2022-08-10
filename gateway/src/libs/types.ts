import { FastifyInstance, FastifyBaseLogger, FastifyRequest, RawRequestDefaultExpression, RawServerDefault } from "fastify";
import { Server, IncomingMessage, ServerResponse } from "http";
import { TypeBoxTypeProvider } from '@fastify/type-provider-typebox'
import { RouteGenericInterface } from "fastify/types/route";

export type FastifyTypeBoxed = FastifyInstance<
    Server, IncomingMessage, ServerResponse, FastifyBaseLogger, TypeBoxTypeProvider
>

export type ServerSetupOptions = {
  skipLogging: boolean
}

export type FastifyRequestTypebox<TSchema> = FastifyRequest<
  RouteGenericInterface,
  RawServerDefault,
  RawRequestDefaultExpression<RawServerDefault>,
  TSchema,
  TypeBoxTypeProvider
>;