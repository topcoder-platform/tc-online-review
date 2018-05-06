using System.Reflection;
using System.Runtime.CompilerServices;

[assembly: AssemblyTitle("%{Component Name}")]
[assembly: AssemblyDescription("%{component_description}")]
[assembly: AssemblyConfiguration("")]
[assembly: AssemblyCompany("http://www.topcoder.com")]
[assembly: AssemblyProduct("TopCoder .NET Component Catalog")]
[assembly: AssemblyCopyright("Copyright © %{current_year}, TopCoder, Inc. All rights reserved.")]
[assembly: AssemblyTrademark("")]
[assembly: AssemblyCulture("")]
[assembly: AssemblyVersion("%{version.major}.%{version.minor}.%{version.micro}")]

// This will not compile with Visual Studio.  If you want to build a signed
// executable use the NAnt build file.  To build under Visual Studio just
// exclude this file from the build.
[assembly: AssemblyDelaySign(false)]
//[assembly: AssemblyKeyFile(@"..\tcs\bin\TopCoder.snk")]
[assembly: AssemblyKeyName("")]
