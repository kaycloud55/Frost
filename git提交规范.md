

### 好处

1. 提供更多的历史信息，方便快速浏览
2. 可以过滤某些commit，便于查找信息
3. 可以直接从commit生成change log


### 格式

```
<type>(<scope>): <subject>
//空一行
<body>
//空一行
<footer>
```
其中，Header是必需的，Body和Footer可以省略.

不管是哪一个部分，任何一行都不得超过72个字符。只是为了避免换行影响美观。

#### Header

Header部分只有一行，包括三个字段`type`（必需）、`scope`（可选）、`subject`（必需）。

* type：用于说明commit的类型，只允许使用下面标识
    * feature: 新功能
    * fix: 修补bug
    * docs：文档（documentation）
    * style： 格式（不影响代码运行的变动）
    * refactor：重构（即不是新增功能，也不是修改bug的代码变动）
    * test：增加测试
    * chore：构建过程或辅助工具的变动

如果type为`feat`和`fix`，则该 commit 将肯定出现在 Change log 之中。其他情况（`docs`、`chore`、`style`、`refactor`、`test`）建议是不要。
* scope：用于说明commit影响的范围，比如数据层、控制层、视图层等等。
* subject：是commit目的的简短描述
    * 以动词开头
    * 第一个字母小写
    * 结尾不加句号
    
    
#### Body

Body 部分是对本次 commit 的详细描述，可以分成多行。下面是一个范例。

```text
More detailed explanatory text, if necessary.  Wrap it to 
about 72 characters or so. 

Further paragraphs come after blank lines.

- Bullet points are okay, too
- Use a hanging indent
```
有两个注意点。

（1）使用第一人称现在时，比如使用change而不是changed或changes。

（2）应该说明代码变动的动机，以及与以前行为的对比。


#### Footer

Footer只用于两种情况
1. 不兼容变动
2. 关闭Issue

如果当前代码与上一个版本不兼容，则 Footer 部分以BREAKING CHANGE开头，后面是对变动的描述、以及变动理由和迁移方法。

```text
BREAKING CHANGE: isolate scope bindings definition has changed.

    To migrate the code follow the example below:

    Before:

    scope: {
      myAttr: 'attribute',
    }

    After:

    scope: {
      myAttr: '@',
    }

    The removed `inject` wasn't generaly useful for directives so there should be no code using it.
```

```

Closes #123, #245, #992
```



##### ***使用插件`Git Commit Template`进行提交。***