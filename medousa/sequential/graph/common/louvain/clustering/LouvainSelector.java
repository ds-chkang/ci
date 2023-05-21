/* MIT License

Copyright (c) 2018 Neil Justice

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package medousa.sequential.graph.common.louvain.clustering;

import medousa.sequential.graph.common.louvain.graph.Graph;
import medousa.sequential.graph.common.louvain.graph.GraphBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Runs the louvain detector the set number of times and writes out the
 * partition data.
 */
public class LouvainSelector implements Clusterer {

  private final Random rnd = new Random();
  private final PartitionWriter writer = new PartitionWriter();
  private final String fileToRead;
  private final String fileToWrite;

  public LouvainSelector(String fileToRead, String fileToWrite) {
    this.fileToRead = fileToRead;
    this.fileToWrite = fileToWrite;
  }

  @Override
  public LayeredCommunityStructure cluster() {
    return cluster(10);
  }

  public LayeredCommunityStructure cluster(int times) {
    long seed;
    double maxMod = 0d;
    double mod;
    List<int[]> output = new ArrayList<>();

    for (int i = 0; i < times; i++) {
      seed = rnd.nextLong();
      final Graph g = new GraphBuilder().fromFile(fileToRead);
      final LouvainDetector detector = new LouvainDetector(g, seed);
      detector.cluster();
      mod = detector.modularity();
      if (mod > maxMod) {
        maxMod = mod;
        output = detector.getCommunities();
      }
    }

    writer.write(output, fileToWrite);
    return new LayeredCommunityStructure(output);
  }

}