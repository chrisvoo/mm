#!/usr/bin/env node
import dotenv from "dotenv"
import { program } from 'commander';
import chalk from 'chalk';

const res = dotenv.config({ path: '../.env' })
if (res.error) {
  console.error(chalk.red(res.error.message))
  process.exit(1)
}

import InfoCatcher from './InfoCatcher.js';

program
  .name('mm')
  .description('CLI for MyMusic')
  .version('0.0.1')
  .configureOutput({
    outputError: (str, write) => write(chalk.red(str))
  });

program
  .command('save')
  .description('retrieve and save band\'s information in the database')
  .argument('<band>', 'band\'s name')
  .option('-d, --dry-run', 'Print results to the screen without saving anything into the db', false)
  .action(async (band: string, options): Promise<void> => {
    const info = new InfoCatcher(band, options)
    return info.run()
  });

program.parseAsync();