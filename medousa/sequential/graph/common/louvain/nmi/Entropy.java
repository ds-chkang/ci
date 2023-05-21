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

package medousa.sequential.graph.common.louvain.nmi;

import medousa.sequential.graph.common.louvain.exception.LouvainException;

public class Entropy {

  /**
   * measures the shannon entropy H of a prob. dist.
   */
  public static double entropy(double[] dist) {
    return entropy(dist, 2);
  }

  /**
   * if base == length of dist, this normalises entropy to within the range 0-1.
   */
  public static double entropy(double[] dist, int base) {
    if (base <= 0) {
      throw new LouvainException("Base cannot be zero or negative");
    }

    double H = 0d;
    for (double v : dist) {
      if (v != 0d) {
        H -= v * Math.log(v);
      }
    }
    return H / Math.log(base);
  }
}