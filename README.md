# eSPiGA
eSPiGA: A Population Genomic Analyses Package with Graphical Interface 

eSPIGA is a software package designed for the analysis of genome variability of multiple populations, specifically focused on the analyses of variant frequencies. The package can work with multiple format alignment (gVCF, fasta, tFasta and ms), annotation files (GFF/GTF) and additional filter files. A common problem using High-Throughput Sequence data is to deal with large quantity of missing data, which can substantially restrict the analysis of variability to few regions, or perform it with a reduced number of statistics. Here, the core of the package, mstatspop, calculates an extended number of population and variability-related statistics and neutrality test accounting for positions including missing data. In addition, optimal neutrality tests are for first time implemented. A number of file outputs are available, including simple text tables, Site Frequency Spec- trum for one or several populations, output files to be used in other softwares, file showing sliding window stats and fully extended formats. Finally, filtering options are available to obtain the desired output.

eSPIGA provides a user-friendly graphical user interface (GUI), which helps to the user to chose the options and necessary flags for the analysis of sequence variability. The final result is a file with all the statistics obtained from the performed analysis. These results can be showed in a tab-formatted table in the interface. The interface is divided in three main sections: (i) pre-processing: where sequence format converters, annotation files and additional filtered op- tions are managed to be ready for the analysis with mstatspop program, (ii) anal- ysis: calculation of all statistics given the desired options of the user, including sequence and annotation files, and (iii) post-processing: the obtained statistics are showed and can be filtered from the whole output file(s) and released in text tab separated tables. A window including all the selected commands are visualised at the bottom of the interface. these commands can be copied and included in a file or directly run on a terminal. This framework allows a fast automation of pipelines for posterior analysis.

URL
http://www.github.com/CRAGENOMICA/GSAW

License
Copyright 2009-2020 Sebastian E. Ramos-Onsins
eSPIGA graphical interface is a free software, under any of the following licenses: LGPL version 3, GPL version 2 and GPL version 3. All users have the rights to obtain, modify and redistribute the full source code of your application. Your users are granted rights founded on the four freedoms of the GNU General Pub- lic License. Any modification to a Qt component covered by the GNU Lesser General Public License must be contributed back to the community. This is the primary open source Qt license, which covers the majority of Qt modules.
All C, C++ and perl packages included in eSPIGA are free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
All packages included in eSPIGA s distributed in the hope that it will be use- ful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public Licenses (http://www.gnu.org/licenses/) for more details.

Installation 

To download the package, there are two options, either clone the repository, that is, copying the git command to execute on a terminal:

git clone https://github.com/CRAGENOMICA/GSAW.git

Follow the intructions using the manual included in the repository.


