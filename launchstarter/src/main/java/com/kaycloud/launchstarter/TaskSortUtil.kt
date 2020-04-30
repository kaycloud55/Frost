package com.kaycloud.launchstarter

/**
 * author: jiangyunkai
 * Created_at: 2020/4/30
 * 主要是对添加到allTaskList中的所有的Task进行排序
 *
 * 1. 先根据每个task的依赖关系[Task.dependOns]来找到它依赖的Task，针对每个它依赖的Task,去找对应的index。然后把找到的index作为from，当前task的index本身作为to，添加到Graph中。
 * 2. 对graph进行拓扑排序
 * 3. 对拓扑排序后的结果进行优化：
 *   1. 找出需要提高优先级的task，作为第二序列。
 *   2. 被依赖的task作为第一序列
 *   3. 需要被等待的task作为第三序列
 *   4. 没有依赖的作为第四序列
 */
object TaskSortUtil {

    private val newTaskHigh = mutableListOf<Task>() //高优先级的Task

    fun getSortResult(originTasks: List<Task>, clzLaunchTasks: List<Class<Task>>): List<Task> {
        val makeTime = System.currentTimeMillis()

        val dependedSet =
            mutableSetOf<Int>() //记录被依赖的Task在allTasks中的位置,因为一个task可能被几个task依赖，所以要用set存储
        val graph = Graph(originTasks.size)

        originTasks.forEachIndexed lit@{ index, task ->
            if (task.isDispatched || task.dependOns() == null || task.dependOns()!!.isEmpty()) {
                return@lit
            }
            for (clz in task.dependOns()!!) {
                val indexOfDepend = getIndexOfTask(originTasks, clzLaunchTasks, clz)
                if (indexOfDepend < 0) {
                    throw IllegalStateException("${task.javaClass.simpleName} depends on ${clz.simpleName} can not be found in tasks")
                }
                dependedSet.add(indexOfDepend)
                graph.addEdge(indexOfDepend, index)
            }
        }

        val indexList = graph.topologicalSort()

        val newTasksAll = getResultTasks(originTasks, dependedSet, indexList)

        DispatcherLog.i("task analyze cost makeTime: ${System.currentTimeMillis() - makeTime}")

        return newTasksAll
    }

    private fun printAllTaskName(newTasksAll: List<Task>) {
        for (task in newTasksAll) {
            DispatcherLog.i(task.javaClass.simpleName)
        }
    }

    private fun getResultTasks(
        originTasks: List<Task>,
        dependSet: Set<Int>,
        indexList: List<Int>
    ): List<Task> {
        val newTaskAll = ArrayList<Task>(originTasks.size)
        val newTasksDepended = ArrayList<Task>() //被别人依赖的
        val newTasksWithoutDepend = ArrayList<Task>() //没有依赖的
        val newTasksAsSoon = ArrayList<Task>() //需要提升自己优先级的，先执行

        for (index in indexList) {
            //被其他task依赖
            if (dependSet.contains(index)) {
                newTasksDepended.add(originTasks[index])
            } else {
                val task = originTasks[index]
                if (task.needRunAsSoon()) {
                    newTasksAsSoon.add(task)
                } else {
                    newTasksWithoutDepend.add(task)
                }
            }
        }

        // 顺序：被别人依赖的 -> 需要提升自己优先级的 -> 需要被等待的 -> 没有依赖的
        newTaskHigh.addAll(newTasksDepended)
        newTaskHigh.addAll(newTasksAsSoon)
        newTaskAll.addAll(newTaskHigh)
        newTaskAll.addAll(newTasksWithoutDepend)
        return newTaskAll
    }

    private fun getIndexOfTask(
        originTasks: List<Task>,
        clzLaunchTasks: List<Class<Task>>,
        clz: Class<Task>
    ): Int {
        val index = clzLaunchTasks.indexOf(clz)
        if (index >= 0) {
            return index
        }

        val size = originTasks.size
        originTasks.forEachIndexed { index, task ->
            if (clz.simpleName == task.javaClass.simpleName) {
                return index
            }
        }
        return index
    }
}