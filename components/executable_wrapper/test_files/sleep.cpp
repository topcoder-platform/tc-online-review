/**
 * sleep.cpp
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 * @author adic
 * @version 1.0
 *
 * This is an additional executable used to stress test the Executable Wrapper component.
 * It takes one integer parameter from the command line representing
 * the number of seconds to wait before exiting.
 */

#include <stdio.h>
#include <time.h>

int main(int argc,char **argv)
{
   if (argc!=2)
   {
      printf("sleep seconds\n");
      return 1;
   }
   else
   {
      int seconds;
      sscanf(argv[1],"%d",&seconds);
      clock_t tic=clock();
      while (true)
      {
         clock_t toc=clock();
         if (toc-tic>seconds*CLK_TCK) break;
      }
      return 0;
   }
}
