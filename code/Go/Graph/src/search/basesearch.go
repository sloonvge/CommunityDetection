package search

import (
	"graph"
)

/** 
* Created by wanjx in 2018/12/8 22:51
**/

type BaseSearch struct {
	marked []bool

}

func NewSearch(g *graph.Graph, s int) {

}

func (s *BaseSearch) Marked(v int) (ret bool) {
	ret = s.marked[v]
	return
}

func (s *BaseSearch) Count() (ret int) {
	for _, m := range s.marked {
		if m == true {
			ret += 1
		}
	}
	return
}

//func Search(g *graph.Graph, s int) {
//	for k, _ := range g.Adj {
//		if marker[s, k] {
//			fmt.Println(v + " ")
//		}
//	}
//}
