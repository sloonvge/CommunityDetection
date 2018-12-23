package graph

/** 
* Created by wanjx in 2018/12/8 22:26
**/

type Graph struct {
	Vetex int
	Edge int
	Adj map[int][]int
}

func (g *Graph) V() (v int){
	v = g.Vetex
	return
}

func (g *Graph) E() (e int) {
	e = g.Edge
	return
}

func (g *Graph) AddEdge(v int, w int) {
	g.Adj[v] = append(g.Adj[v], w)
	g.Adj[w] = append(g.Adj[w], v)
	g.Edge++
}

func (g *Graph) Adjs(v int) (ret []int) {
	ret = g.Adj[v]
	return
}

func (g *Graph) Degree() (ret int) {
	for _, v := range g.Adj {
		ret += len(v)
	}
	return
}
