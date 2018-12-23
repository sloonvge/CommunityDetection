#!/usr/bin/env python
# _*_ coding:utf-8 _*_

import networkx as nx
import random
import collections

class LPA():
    def __init__(self, G, MaxIter = 20):
        self._G = G
        self._N = len(G.node)
        self._MaxIter = MaxIter

    def lpa(self):
        for i in range(self._N):
            self._G.node[i]["label"] = i
        iterTime = 0
        while (not self.stopSpread() and iterTime < self._MaxIter):
            self.spreadLabel()
            iterTime += 1
        return self.getCommunities()

    def spreadLabel(self):
        visit = random.sample(self._G.nodes(), len(self._G.nodes()))
        for i in visit:
            node = self._G.node[i]
            label = node["label"]
            mostLabels = self.getMostNeighborLabel(i)
            if label not in mostLabels:
                newLabel = random.choice(mostLabels)
                node["label"] = newLabel

    def stopSpread(self):
        for i in range(self._N):
            node  = self._G.node[i]
            label = node["label"]
            mostLabels = self.getMostNeighborLabel(i)
            if label not in mostLabels:
                return False
        return True

    def getMostNeighborLabel(self, index):
        adjLabels = collections.defaultdict(int)
        for adj in self._G.neighbors(index):
            adjLabel = self._G.node[adj]["label"]
            adjLabels[adjLabel] += 1
        maxAdjLabels = max(adjLabels.values())
        return [i[0] for i in adjLabels.items() if i[1] == maxAdjLabels]

    def getCommunities(self):
        commu = collections.defaultdict(lambda: list())
        for node in self._G.nodes(True):
            label = node[1]["label"]
            commu[label].append(node[0])
        return commu.values()


if __name__ == "__main__":
    G = nx.karate_club_graph()
    op = LPA(G, 100)
    com = op.lpa()
    print(com)