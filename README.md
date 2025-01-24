# eSPiGA
eSPiGA: A Population Genomic Analyses Package with Graphical Interface 

eSPIGA is a software package designed for the analysis of genome variability of multiple populations, specifically focused on the analyses of variant frequencies. The package can work with multiple format alignment (gVCF, fasta, tFasta and ms), annotation files (GFF/GTF) and additional filter files. A common problem using High-Throughput Sequence data is to deal with large quantity of missing data, which can substantially restrict the analysis of variability to few regions, or perform it with a reduced number of statistics. Here, the core of the package, mstatspop, calculates an extended number of population and variability-related statistics and neutrality test accounting for positions including missing data. In addition, optimal neutrality tests are for first time implemented. A number of file outputs are available, including simple text tables, Site Frequency Spec- trum for one or several populations, output files to be used in other softwares, file showing sliding window stats and fully extended formats. Finally, filtering options are available to obtain the desired output.

eSPIGA provides a user-friendly graphical user interface (GUI), which helps to the user to chose the options and necessary flags for the analysis of sequence variability. The final result is a file with all the statistics obtained from the performed analysis. These results can be showed in a tab-formatted table in the interface. The interface is divided in three main sections: (i) pre-processing: where sequence format converters, annotation files and additional filtered op- tions are managed to be ready for the analysis with mstatspop program, (ii) anal- ysis: calculation of all statistics given the desired options of the user, including sequence and annotation files, and (iii) post-processing: the obtained statistics are showed and can be filtered from the whole output file(s) and released in text tab separated tables. A window including all the selected commands are visualised at the bottom of the interface. these commands can be copied and included in a file or directly run on a terminal. This framework allows a fast automation of pipelines for posterior analysis.




# Installation 
## 1. Download Desktop app from 
https://updates.biotechvana.com/software/eSPiGA/latest/

## eSPiGA Downloads Latest Versions
1. **Windows (64-bit)**
   - File: [eSPiGA.latest-win32.win32.x86_64.zip](https://updates.biotechvana.com/software/eSPiGA/latest/eSPiGA.latest-win32.win32.x86_64.zip)

2. **macOS (64-bit)**
   - File: [eSPiGA.latest-macosx.cocoa.x86_64.pkg](https://updates.biotechvana.com/software/eSPiGA/latest/eSPiGA.latest-macosx.cocoa.x86_64.pkg)

3. **Linux (64-bit)**
   - File: [eSPiGA.latest-linux.gtk.x86_64.zip](https://updates.biotechvana.com/software/eSPiGA/latest/eSPiGA.latest-linux.gtk.x86_64.zip)

## 2.  Run SPIGA server using Docker Hub

## 1. Pull the Image
First, pull the SPIGA image from Docker Hub:
```bash
docker pull biotechvana/spiga
```

## 2. Run the Container
Run the SPIGA container with the following command:

```bash
docker run -p 8080:80 -p 2202:22 \
-e GPRO_USER_ID=$(id -u) \
-e GPRO_USER_GID=$(id -g) \
-v /path/to/your/data:/data/gpro_user \
biotechvana/spiga
```

### Command Explanation:
- `-p 8080:80`: Maps container's port 80 to host's port 8080 (web interface)
- `-p 2202:22`: Maps container's SSH port 22 to host's port 2202
- `-e GPRO_USER_ID=$(id -u)`: Sets user ID to match host system user
- `-e GPRO_USER_GID=$(id -g)`: Sets group ID to match host system group
- `-v /path/to/your/data:/data/gpro_user`: Mounts your local data directory into the container

## 3. Access SPIGA
- Open App and set connection setting for 
- Server : localhost
- Username : g_user (or your setting)
- Password : g_user (or your setting)
- SSH access: Port 2202 (your mapping)
- API Port : 8080 (your mapping)

## 4. Data Persistence
Your data will be persisted in the mounted volume at `/path/to/your/data`

## Note
Make sure to replace `/path/to/your/data` with your actual data directory path.



License
Copyright 2009-2020 Sebastian E. Ramos-Onsins
eSPIGA graphical interface is a free software, under any of the following licenses: LGPL version 3, GPL version 2 and GPL version 3. All users have the rights to obtain, modify and redistribute the full source code of your application. Your users are granted rights founded on the four freedoms of the GNU General Pub- lic License. Any modification to a Qt component covered by the GNU Lesser General Public License must be contributed back to the community. This is the primary open source Qt license, which covers the majority of Qt modules.
All C, C++ and perl packages included in eSPIGA are free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
All packages included in eSPIGA s distributed in the hope that it will be use- ful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public Licenses (http://www.gnu.org/licenses/) for more details.




