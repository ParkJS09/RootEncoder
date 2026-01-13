/**
 * 프레임 타입 학습 예제
 *
 * I-frame (키프레임) vs P-frame (예측 프레임)
 */

// ==========================================
// 예제 1: 프레임 타입 기본 개념
// ==========================================

fun understandFrameTypes() {
    println("╔════════════════════════════════════════╗")
    println("║   프레임 타입: I-frame vs P-frame     ║")
    println("╚════════════════════════════════════════╝\n")

    println("비디오 압축의 핵심 아이디어:")
    println("→ 모든 프레임을 완전히 저장하지 않고, 변화만 저장!\n")

    println("🔑 I-Frame (Intra-coded frame)")
    println("   = 키프레임 = IDR (Instantaneous Decoder Refresh)")
    println("   - 완전한 이미지 (JPEG 같은 정지 이미지)")
    println("   - 다른 프레임 없이 독립적으로 디코딩 가능")
    println("   - 크기: 크다 (예: 200KB)")
    println("")

    println("⚡ P-Frame (Predicted frame)")
    println("   = 예측 프레임")
    println("   - 이전 프레임과의 차이만 저장")
    println("   - 이전 프레임 필요 (독립적으로 디코딩 불가)")
    println("   - 크기: 작다 (예: 10KB, I-frame의 1/20)")
    println("")

    println("🎬 B-Frame (Bidirectional frame)")
    println("   = 양방향 예측 프레임")
    println("   - 이전 + 이후 프레임 모두 참조")
    println("   - 가장 효율적이지만 복잡함")
    println("   - RootEncoder는 주로 I/P 사용")
}

// ==========================================
// 예제 2: GOP (Group of Pictures) 이해
// ==========================================

fun demonstrateGOP() {
    println("\n=== GOP (Group of Pictures) ===\n")

    println("GOP = I-frame부터 다음 I-frame 직전까지의 프레임 그룹\n")

    val fps = 30
    val iFrameInterval = 2  // 2초
    val gopSize = fps * iFrameInterval  // 60 프레임

    println("설정: ${fps}fps, ${iFrameInterval}초 간격")
    println("GOP 크기: $gopSize 프레임\n")

    println("프레임 시퀀스:")
    println("┌────────────────────────────────────────┐")

    print("│ ")
    for (i in 0 until 20) {
        if (i % gopSize == 0) {
            print("I ")
        } else {
            print("P ")
        }
    }
    println("...")
    println("│ 0  1  2  3  4  5  6  7  8  9  10...")
    println("└────────────────────────────────────────┘")
    println("   ↑                            ↑")
    println("   GOP 1                        GOP 2")
}

// ==========================================
// 예제 3: 크기 비교
// ==========================================

fun compareSizes() {
    println("\n=== 프레임 크기 비교 ===\n")

    val resolution = "1920×1080"
    val iFrameSize = 200  // KB
    val pFrameSize = 10   // KB

    println("해상도: $resolution")
    println("")
    println("I-frame: ${iFrameSize} KB (전체 이미지)")
    println("P-frame: ${pFrameSize} KB (차이만)")
    println("압축률: ${iFrameSize / pFrameSize}배\n")

    println("2초 GOP (60프레임) 계산:")
    val gopSize = 60
    val gopWithAllI = iFrameSize * gopSize
    val gopWithIP = iFrameSize + (pFrameSize * (gopSize - 1))

    println("- 모두 I-frame: ${gopWithAllI} KB")
    println("- 1 I + 59 P:  ${gopWithIP} KB")
    println("- 절약: ${gopWithAllI - gopWithIP} KB (${String.format("%.0f", (1 - gopWithIP.toFloat() / gopWithAllI) * 100)}%)")
}

// ==========================================
// 예제 4: NAL Unit 타입 파싱
// ==========================================

fun parseNalUnitType(nalByte: Byte): Pair<String, String> {
    // H.264 NAL Unit 헤더:
    // | F(1) | NRI(2) | Type(5) |
    //   ↑      ↑        ↑
    //   금지   우선순위  타입 (0-31)

    val nalType = (nalByte.toInt() and 0x1F)  // 하위 5비트

    val typeName = when (nalType) {
        1 -> "SLICE (P-frame)"
        5 -> "IDR (I-frame, 키프레임)"
        6 -> "SEI (보조 정보)"
        7 -> "SPS (Sequence Parameter Set)"
        8 -> "PPS (Picture Parameter Set)"
        9 -> "AUD (Access Unit Delimiter)"
        else -> "기타 (${nalType})"
    }

    val description = when (nalType) {
        1 -> "예측 프레임 (이전 프레임 참조)"
        5 -> "키프레임 (독립적으로 디코딩 가능)"
        7 -> "비디오 설정 정보 (해상도, 프로파일 등)"
        8 -> "픽처 파라미터 (슬라이스 그룹 등)"
        else -> ""
    }

    return Pair(typeName, description)
}

fun demonstrateNalParsing() {
    println("\n=== NAL Unit 타입 파싱 ===\n")

    val nalBytes = listOf<Byte>(
        0x65,  // 01100101 → Type 5 (IDR)
        0x41,  // 01000001 → Type 1 (SLICE)
        0x67,  // 01100111 → Type 7 (SPS)
        0x68   // 01101000 → Type 8 (PPS)
    )

    println("NAL 헤더 구조: | F(1bit) | NRI(2bit) | Type(5bit) |\n")

    for (nalByte in nalBytes) {
        val (typeName, description) = parseNalUnitType(nalByte)
        val binary = String.format("%8s", Integer.toBinaryString(nalByte.toInt() and 0xFF))
            .replace(' ', '0')

        println("0x${String.format("%02X", nalByte)} = $binary")
        println("  → $typeName")
        if (description.isNotEmpty()) {
            println("     $description")
        }
        println("")
    }
}

// ==========================================
// 예제 5: I-Frame 간격 선택
// ==========================================

fun chooseIFrameInterval() {
    println("\n=== I-Frame 간격 선택 가이드 ===\n")

    val scenarios = listOf(
        Triple(
            "라이브 스트리밍",
            2,
            "빠른 복구, 적당한 크기"
        ),
        Triple(
            "비디오 통화",
            1,
            "낮은 지연, 빠른 에러 복구"
        ),
        Triple(
            "YouTube 업로드",
            3,
            "효율적 압축, seek 가능"
        ),
        Triple(
            "파일 저장",
            10,
            "최대 압축, seek 느림"
        ),
        Triple(
            "보안 카메라",
            5,
            "저장 공간 절약"
        )
    )

    for ((scenario, interval, reason) in scenarios) {
        val gop = interval * 30  // 30fps 기준
        println("${scenario.padEnd(20)} ${interval}초 (${gop} 프레임)")
        println("  → $reason\n")
    }

    println("💡 트레이드오프:")
    println("   짧은 간격: 크기↑, 품질↑, seek↑, 복구↑")
    println("   긴 간격:   크기↓, 품질↓, seek↓, 복구↓")
}

// ==========================================
// 예제 6: 실시간 프레임 시퀀스 시뮬레이션
// ==========================================

data class FrameInfo(val index: Int, val type: String, val size: Int, val timestamp: Double)

fun simulateFrameSequence(fps: Int, duration: Int, iFrameInterval: Int): List<FrameInfo> {
    val frames = mutableListOf<FrameInfo>()
    val totalFrames = fps * duration
    val gopSize = fps * iFrameInterval

    for (i in 0 until totalFrames) {
        val isIFrame = (i % gopSize == 0)
        val type = if (isIFrame) "I" else "P"
        val size = if (isIFrame) 200 else (8..15).random()  // KB
        val timestamp = i.toDouble() / fps

        frames.add(FrameInfo(i, type, size, timestamp))
    }

    return frames
}

fun demonstrateFrameSequence() {
    println("\n=== 프레임 시퀀스 시뮬레이션 ===\n")

    val frames = simulateFrameSequence(30, 5, 2)

    println("설정: 30fps, 5초, 2초 I-frame 간격\n")
    println("프레임  타입  크기(KB)  시간(초)  누적(KB)")
    println("─────────────────────────────────────────")

    var cumulative = 0
    for (frame in frames.take(20)) {  // 처음 20프레임만
        cumulative += frame.size
        val marker = if (frame.type == "I") "🔑" else "  "
        println("${marker} ${String.format("%3d", frame.index)}".padEnd(10) +
                "${frame.type}".padEnd(7) +
                "${String.format("%3d", frame.size)}".padEnd(11) +
                "${String.format("%.2f", frame.timestamp)}".padEnd(11) +
                cumulative)
    }

    println("...")
    println("\n전체 통계:")
    val totalSize = frames.sumOf { it.size }
    val iFrameCount = frames.count { it.type == "I" }
    val pFrameCount = frames.count { it.type == "P" }

    println("- 총 프레임: ${frames.size}")
    println("- I-frame: $iFrameCount (${String.format("%.1f", iFrameCount.toFloat() / frames.size * 100)}%)")
    println("- P-frame: $pFrameCount (${String.format("%.1f", pFrameCount.toFloat() / frames.size * 100)}%)")
    println("- 총 크기: ${totalSize} KB (${totalSize / 1024} MB)")
    println("- 평균 비트레이트: ${totalSize * 8 / 5 / 1024} Mbps")
}

// ==========================================
// 예제 7: 키프레임 요청 (동적)
// ==========================================

fun demonstrateKeyframeRequest() {
    println("\n=== 키프레임 동적 요청 ===\n")

    println("정기 I-frame 외에 추가로 키프레임을 요청하는 경우:\n")

    println("1️⃣  장면 전환 감지")
    println("   - 프레임 간 차이가 클 때")
    println("   - 예: 카메라 화면 급격한 변화")
    println("")

    println("2️⃣  네트워크 에러 복구")
    println("   - 패킷 손실 후 화면 깨짐")
    println("   - 새 키프레임으로 복구")
    println("")

    println("3️⃣  새 시청자 진입")
    println("   - 라이브 스트림에 새로 접속")
    println("   - 즉시 키프레임 전송")
    println("")

    println("4️⃣  Seek 동작")
    println("   - 사용자가 타임라인 이동")
    println("   - 가장 가까운 I-frame으로 점프")
    println("")

    println("RootEncoder 코드:")
    println("```kotlin")
    println("// VideoEncoder에서 키프레임 요청")
    println("fun requestKeyFrame() {")
    println("    val params = Bundle()")
    println("    params.putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0)")
    println("    codec?.setParameters(params)")
    println("}")
    println("```")
}

// ==========================================
// 예제 8: RootEncoder에서의 실제 사용
// ==========================================

fun rootEncoderFrameFlow() {
    println("\n=== RootEncoder 프레임 처리 흐름 ===\n")

    println("1️⃣  VideoEncoder.prepareVideoEncoder()")
    println("   videoFormat.setInteger(KEY_I_FRAME_INTERVAL, 2)")
    println("   → MediaCodec에 2초 간격 지시")
    println("   ↓")

    println("2️⃣  카메라 프레임 입력")
    println("   onImageAvailable() → YUV 데이터")
    println("   ↓")

    println("3️⃣  MediaCodec 인코딩")
    println("   - 자동으로 I/P 프레임 결정")
    println("   - NAL Unit으로 출력")
    println("   ↓")

    println("4️⃣  H264Packet.createFlvPacket()")
    println("   ```kotlin")
    println("   val type = (validBuffer.get(0) and 0x1F).toInt()")
    println("   if (type == VideoNalType.IDR.value) {")
    println("       nalType = KEYFRAME  // I-frame")
    println("   } else {")
    println("       nalType = INTER_FRAME  // P-frame")
    println("   }")
    println("   ```")
    println("   ↓")

    println("5️⃣  FLV 패킷 생성")
    println("   header[0] = (nalType << 4) | AVC")
    println("   KEYFRAME(1) 또는 INTER_FRAME(2)")
    println("   ↓")

    println("6️⃣  RTMP 전송")
    println("   rtmpClient.sendVideo(buffer)")
}

// ==========================================
// 메인 함수
// ==========================================

fun main() {
    understandFrameTypes()
    demonstrateGOP()
    compareSizes()
    demonstrateNalParsing()
    chooseIFrameInterval()
    demonstrateFrameSequence()
    demonstrateKeyframeRequest()
    rootEncoderFrameFlow()

    println("\n" + "=".repeat(60))
    println("프레임 타입 학습 완료! 🎬")
    println("=".repeat(60))
}

/**
 * 퀴즈:
 *
 * Q1. I-frame와 P-frame의 가장 큰 차이는?
 * A: I-frame은 독립적으로 디코딩 가능, P-frame은 이전 프레임 필요
 *
 * Q2. GOP란 무엇인가?
 * A: Group of Pictures. I-frame부터 다음 I-frame 직전까지의 프레임 그룹
 *
 * Q3. 30fps, 2초 I-frame 간격이면 GOP 크기는?
 * A: 30 × 2 = 60 프레임
 *
 * Q4. I-frame 간격을 1초에서 10초로 늘리면?
 * A: 파일 크기는 줄지만, seek가 느려지고 에러 복구가 어려워짐
 *
 * Q5. NAL Unit 헤더에서 타입을 추출하는 방법은?
 * A: nalByte & 0x1F (하위 5비트)
 *    Type 5 = IDR (I-frame)
 *    Type 1 = SLICE (P-frame)
 *
 * Q6. 스트리밍 중 화면이 깨지면 어떻게 복구?
 * A: 새 I-frame을 요청하여 전체 이미지를 다시 받음
 *
 * 핵심 원리:
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * 비디오 압축 = 시간적 중복 제거
 * I-frame = 공간적 압축만 (JPEG 같은)
 * P-frame = 공간적 + 시간적 압축 (차이만 저장)
 * → 15~20배 압축 효율!
 */
