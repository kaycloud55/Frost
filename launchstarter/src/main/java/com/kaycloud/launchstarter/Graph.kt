package com.kaycloud.launchstarter

import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

/**
 * author: jiangyunkai
 * Created_at: 2020/4/29
 */
class Graph(private val vertexCount: Int) {


    private val adjacencyList =
        ArrayList<LinkedList<Int>>(vertexCount) //每个元素存储的是某个顶点可达的顶点链表,这个链表存储的元素是顶点在图中的索引


    init {
        adjacencyList.forEachIndexed { index, _ ->
            adjacencyList[index] = LinkedList()
        }
    }

    /**
     * 添加边
     */
    fun addEdge(from: Int, to: Int) {
        if (from !in 0..adjacencyList.lastIndex) {
            return
        }
        if (to !in 0..adjacencyList.lastIndex) {
            return
        }
        adjacencyList[from].add(to)
    }

    /**
     * 图的拓扑排序
     */
    fun topologicalSort(): ArrayList<Int> {
        // 1. 统计所有点的入度，value表示每个点的入度
        val inDegreeList = ArrayList<Int>(vertexCount)
        adjacencyList.forEach { nodeIndexList ->
            nodeIndexList.forEach { nodeIndex ->
                inDegreeList[nodeIndex]++
            }
        }
        // 2. 找出所有入度为0的节点
        val queue = LinkedList<Int>()
        inDegreeList.forEachIndexed { index, inDegree ->
            if (inDegree == 0) {
                queue.add(index)
            }
        }
        // 3. 从入度为0的点出发，对每个节点的入度依次减1，达到0就加入queue中
        var count = 0
        val sortResult = arrayListOf<Int>()
        while (queue.isNotEmpty()) {
            val nodeIndex = queue.poll()!!
            sortResult.add(nodeIndex)
            adjacencyList[nodeIndex].forEach { endPointIndex ->
                if (--inDegreeList[endPointIndex] == 0) {
                    queue.add(endPointIndex)
                }
            }
            count++
        }
        if (count != vertexCount) {
            throw IllegalStateException("Exists a cycle in the graph")
        }
        return sortResult
    }

}