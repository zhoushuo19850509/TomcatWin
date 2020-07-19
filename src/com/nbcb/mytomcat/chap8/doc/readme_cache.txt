
这个文档主要是详细说明Loader的cache原理
首先，cache功能在WebappClassLoader中实现




对于cache的原理，其实也比较直接，就是


对于cache功能，我想WAS中应该也会有。
突然想到，我们之前用jarjar方案，避免jar包冲突。这个方案可能会有问题，因为cache方案会把加载过的class缓存起来
那么，jarjar方案就会存在内存使用过高的问题。




