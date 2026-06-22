# Quick Line Ref for CLion

一个给 `CLion` 使用的小插件，用来快速复制当前代码文件或选区的引用。

输出格式：

- 无选区：`@<full-path>/file.cpp`
- 单行选区：`@<full-path>/file.cpp#L64`
- 多行选区：`@<full-path>/file.cpp#L64-70`

这个插件的核心目标是：

- 复制真实磁盘路径，而不是 IDE 工程里的逻辑路径
- 避免 IDE 或 CMake 工程视图里那种不对应真实文件位置的引用
- 用一个快捷键完成复制，方便手动粘贴到 terminal、IM、文档或工单里

## 当前行为

- 快捷键：
  - Linux / Windows：`Ctrl+Alt+R`
  - macOS：`Cmd+Alt+R`
- 右键菜单：`Copy Quick Line Ref`
- 无选区时只复制文件路径
- 有选区时复制带行号或行号范围的引用
- 路径优先使用真实路径（说明：会尽量解析软链接后的真实磁盘位置）

## 安装方式

### 从磁盘安装

1. 在 `CLion` 里打开 `Settings` / `Preferences`
2. 进入 `Plugins`
3. 点击右上角齿轮
4. 选择 `Install Plugin from Disk...`
5. 选择打包产物：

   `build/distributions/quick-line-ref-clion-0.1.0.zip`

6. 重启 `CLion`

## 兼容性

当前工程按 `CLion 2026.1.x` 构建和验证。

- `build.gradle.kts` 里限制了 `sinceBuild = 261`
- 开发基线使用 `Java 21`
- 构建时需要提供本机 `CLion` 安装目录

可以用下面任一方式提供本机 `CLion` 路径：

```bash
export CLION_LOCAL_PATH=<CLION_INSTALL_DIR>
```

或者：

```bash
./gradlew buildPlugin -PclionLocalPath=<CLION_INSTALL_DIR>
```

## 开发

### 运行测试

```bash
CLION_LOCAL_PATH=<CLION_INSTALL_DIR> ./gradlew test
```

### 打包插件

```bash
CLION_LOCAL_PATH=<CLION_INSTALL_DIR> ./gradlew buildPlugin
```

打包结果默认在：

```text
build/distributions/quick-line-ref-clion-0.1.0.zip
```

### 用沙箱方式启动测试 IDE

```bash
CLION_LOCAL_PATH=<CLION_INSTALL_DIR> ./gradlew runIde
```

这会启动一个独立的测试 IDE，不会直接污染你主力使用的 `CLion` 配置。

## 代码结构

```text
src/main/kotlin/com/shawnyang/quicklineref/
├── CopyQuickLineRefAction.kt   # 动作入口，处理快捷键和剪贴板复制
├── PathResolver.kt             # 解析真实磁盘路径
├── ReferenceBuilder.kt         # 根据编辑器状态生成引用
└── ReferenceFormatter.kt       # 统一拼接输出格式
```

## 设计说明

这个插件没有复用 `CLion` 自带的 `Copy Reference` 结果，而是自己从当前编辑器和文件系统生成引用。

这样做的原因是：

- `Copy Reference` 在这个仓库结构下可能会给出 IDE / CMake 工程路径
- 工程路径不一定是磁盘真实路径
- 对外发给人或给其他工具消费时，真实文件路径更稳定

## 示例

假设当前文件真实路径是：

```text
<full-path>/project/src/example.cpp
```

那么复制结果可能是：

```text
@<full-path>/project/src/example.cpp
```

```text
@<full-path>/project/src/example.cpp#L64
```

```text
@<full-path>/project/src/example.cpp#L64-70
```
