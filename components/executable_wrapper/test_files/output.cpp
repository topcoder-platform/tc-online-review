/**
 * output.cpp
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 * @author adic
 * @version 1.0
 *
 * This is an additional executable used to stress test the Executable Wrapper component,
 * more precise the ability to handle large outputs of the executed commands.
 * It takes one integer parameter from the command line representing
 * the number of characters to write to the standard output stream.
 */

#include <stdio.h>
#include <stdlib.h>

int main(int argc,char **argv)
{
   if (argc!=2)
   {
      printf("output stdoutCharCount\n");
      return 1;
   }
   else
   {
      int stdoutCharCount;
      sscanf(argv[1],"%d",&stdoutCharCount);
      //randomize();
      for (int i=0;i<stdoutCharCount;i++) fputc(32+rand()%96,stdout);
      return 0;
   }
}
