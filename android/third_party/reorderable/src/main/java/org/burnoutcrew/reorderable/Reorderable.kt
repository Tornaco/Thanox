/*
 * Copyright 2022 André Claßen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.burnoutcrew.reorderable

import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.util.fastFirstOrNull

fun Modifier.reorderable(
    state: ReorderableState<*>
) = then(
    Modifier.pointerInput(Unit) {
        forEachGesture {
            val dragStart = state.interactions.receive()
            val down = awaitPointerEventScope {
                currentEvent.changes.fastFirstOrNull { it.id == dragStart.id }
            }
            if (down != null && state.onDragStart(down.position.x.toInt(), down.position.y.toInt())) {
                dragStart.offset?.apply {
                    state.onDrag(x.toInt(), y.toInt())
                }
                detectDrag(
                    down.id,
                    onDragEnd = {
                        state.onDragCanceled()
                    },
                    onDragCancel = {
                        state.onDragCanceled()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        state.onDrag(dragAmount.x.toInt(), dragAmount.y.toInt())
                    })
            }
        }
    })

internal suspend fun PointerInputScope.detectDrag(
    down: PointerId,
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit,
) {
    awaitPointerEventScope {
        if (
            drag(down) {
                onDrag(it, it.positionChange())
                it.consume()
            }
        ) {
            // consume up if we quit drag gracefully with the up
            currentEvent.changes.forEach {
                if (it.changedToUp()) it.consume()
            }
            onDragEnd()
        } else {
            onDragCancel()
        }
    }
}

internal data class StartDrag(val id: PointerId, val offset: Offset? = null)