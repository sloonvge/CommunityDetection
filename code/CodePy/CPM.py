#!/usr/bin/env python
# _*_ coding:utf-8 _*_

import collections
import networkx as nx
nx.random_graphs.watts_strogatz_graph()

class CPM():
    def __init__(self, G, K=4):
        self._G = G
        self._K = K

    def cpm(self):
        KCliqueGraph = self.getKClique()
        #newG = self.getOverlapMat(KCliqueGraph)
        return KCliqueGraph

    def getKClique(self):
        allCliques = list(nx.find_cliques(self._G))
        nodeInCliques = collections.defaultdict(lambda:set())
        for i, c in enumerate(allCliques):
            if len(c) < self._K:
                continue
            for n in c:
                nodeInCliques[n].add(i)
        cliqueNeighbor = collections.defaultdict(lambda:set())
        remained = set()
        for i, c in enumerate(allCliques):
            if len(c) < self._K:
                continue
            remained.add(i)
            tempC = set(c)
            tempNeighbors = set()
            for n in c:
                tempNeighbors.update(nodeInCliques[n])
            tempNeighbors.remove(i)
            for j in tempNeighbors:
                cj = allCliques[j]
                if len(cj) < self._K:
                    continue
                if j < i:
                    continue
                tempCj = set(cj)
                if len(tempC & tempCj) >= min(len(tempC), len(tempCj)) -1:
                    cliqueNeighbor[i].add(j)
                    cliqueNeighbor[j].add(i)

        communities = []
        for i, c in enumerate(allCliques):
            if i in remained and len(c) >= self._K:
                communities.append(set(c))
                neighbors = list(cliqueNeighbor[i])
                while len(neighbors) != 0:
                    n = neighbors.pop()
                    if n in remained:
                        communities[len(communities) - 1].update(allCliques[n])
                        remained.remove(n)
                        for nn in cliqueNeighbor[n]:
                            if nn in remained:
                                neighbors.append(nn)
        return communities

    def getOverlapMat(self, KCliques):
        newG = nx.Graph()
        for i in range(len(KCliques)):
            for j in range(len(KCliques)):
                temp = set(KCliques[i]).intersection(KCliques[j])
                if i != j:
                    if len(temp) < self._K-1:
                        newG.add_edge(i, j)
        communities = self.getCommunities(newG, KCliques)

        return communities

    def getCommunities(self, G, KCliques):

        return



if __name__ == "__main__":
    G = nx.karate_club_graph()
    op = CPM(G, 4)
    com = op.cpm()
    for c in com:
        print(c)

