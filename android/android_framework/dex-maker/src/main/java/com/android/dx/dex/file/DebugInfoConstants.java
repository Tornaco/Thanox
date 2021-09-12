/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dx.dex.file;

/** Constants for the dex debug info state machine format. */
public interface DebugInfoConstants {

  /*
   * normal opcodes
   */

  /**
   * Terminates a debug info sequence for a method.
   *
   * <p>Args: none
   */
  int DBG_END_SEQUENCE = 0x00;

  /**
   * Advances the program counter/address register without emitting a positions entry.
   *
   * <p>
   *
   * <p>Args:
   *
   * <ol>
   *   <li>Unsigned LEB128 &mdash; amount to advance pc by
   * </ol>
   */
  int DBG_ADVANCE_PC = 0x01;

  /**
   * Advances the line register without emitting a positions entry.
   *
   * <p>
   *
   * <p>Args:
   *
   * <ol>
   *   <li>Signed LEB128 &mdash; amount to change line register by.
   * </ol>
   */
  int DBG_ADVANCE_LINE = 0x02;

  /**
   * Introduces a local variable at the current address.
   *
   * <p>
   *
   * <p>Args:
   *
   * <ol>
   *   <li>Unsigned LEB128 &mdash; register that will contain local.
   *   <li>Unsigned LEB128 &mdash; string index (shifted by 1) of local name.
   *   <li>Unsigned LEB128 &mdash; type index (shifted by 1) of type.
   * </ol>
   */
  int DBG_START_LOCAL = 0x03;

  /**
   * Introduces a local variable at the current address with a type signature specified.
   *
   * <p>
   *
   * <p>Args:
   *
   * <ol>
   *   <li>Unsigned LEB128 &mdash; register that will contain local.
   *   <li>Unsigned LEB128 &mdash; string index (shifted by 1) of local name.
   *   <li>Unsigned LEB128 &mdash; type index (shifted by 1) of type.
   *   <li>Unsigned LEB128 &mdash; string index (shifted by 1) of type signature.
   * </ol>
   */
  int DBG_START_LOCAL_EXTENDED = 0x04;

  /**
   * Marks a currently-live local variable as out of scope at the current address.
   *
   * <p>
   *
   * <p>Args:
   *
   * <ol>
   *   <li>Unsigned LEB128 &mdash; register that contained local
   * </ol>
   */
  int DBG_END_LOCAL = 0x05;

  /**
   * Re-introduces a local variable at the current address. The name and type are the same as the
   * last local that was live in the specified register.
   *
   * <p>
   *
   * <p>Args:
   *
   * <ol>
   *   <li>Unsigned LEB128 &mdash; register to re-start.
   * </ol>
   */
  int DBG_RESTART_LOCAL = 0x06;

  /**
   * Sets the "prologue_end" state machine register, indicating that the next position entry that is
   * added should be considered the end of a method prologue (an appropriate place for a method
   * breakpoint).
   *
   * <p>
   *
   * <p>The prologue_end register is cleared by any special ({@code >= OPCODE_BASE}) opcode.
   */
  int DBG_SET_PROLOGUE_END = 0x07;

  /**
   * Sets the "epilogue_begin" state machine register, indicating that the next position entry that
   * is added should be considered the beginning of a method epilogue (an appropriate place to
   * suspend execution before method exit).
   *
   * <p>
   *
   * <p>The epilogue_begin register is cleared by any special ({@code >= OPCODE_BASE}) opcode.
   */
  int DBG_SET_EPILOGUE_BEGIN = 0x08;

  /**
   * Sets the current file that that line numbers refer to. All subsequent line number entries make
   * reference to this source file name, instead of the default name specified in code_item.
   *
   * <p>Args:
   *
   * <ol>
   *   <li>Unsigned LEB128 &mdash; string index (shifted by 1) of source file name.
   * </ol>
   */
  int DBG_SET_FILE = 0x09;

  /* IF YOU ADD A NEW OPCODE, increase OPCODE_BASE */

  /*
   * "special opcode" configuration, essentially what's found in
   * the line number program header in DWARFv3, Section 6.2.4
   */

  /** the smallest value a special opcode can take */
  int DBG_FIRST_SPECIAL = 0x0a;

  int DBG_LINE_BASE = -4;
  int DBG_LINE_RANGE = 15;
  // MIN_INSN_LENGTH is always 1
}
