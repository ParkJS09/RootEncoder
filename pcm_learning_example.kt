/**
 * PCM 오디오 학습 예제
 *
 * PCM (Pulse Code Modulation)은 디지털 오디오의 기본 형식입니다.
 */

import kotlin.math.*

// ==========================================
// 예제 1: PCM 기본 개념
// ==========================================

fun understandPCM() {
    println("╔════════════════════════════════════════╗")
    println("║   PCM 오디오 기본 개념                 ║")
    println("╚════════════════════════════════════════╝\n")

    println("PCM = Pulse Code Modulation")
    println("아날로그 소리 → 디지털 숫자 변환\n")

    println("핵심 3요소:")
    println("1. 샘플레이트 (Sample Rate): 1초에 몇 번 측정?")
    println("2. 비트 깊이 (Bit Depth): 각 샘플을 몇 비트로 표현?")
    println("3. 채널 (Channels): 모노(1) vs 스테레오(2)?")
}

// ==========================================
// 예제 2: PCM 데이터 크기 계산
// ==========================================

fun calculatePCMSize(sampleRate: Int, bitDepth: Int, channels: Int, seconds: Int) {
    println("\n=== PCM 데이터 크기 계산 ===")

    val bytesPerSample = bitDepth / 8
    val bytesPerSecond = sampleRate * bytesPerSample * channels
    val totalBytes = bytesPerSecond * seconds

    println("샘플레이트: $sampleRate Hz")
    println("비트 깊이: $bitDepth bit")
    println("채널: $channels")
    println("시간: $seconds 초")
    println("")
    println("계산:")
    println("- 샘플당 바이트: $bytesPerSample bytes")
    println("- 초당 바이트: $bytesPerSecond bytes/s (${bytesPerSecond / 1024} KB/s)")
    println("- 총 크기: $totalBytes bytes (${totalBytes / 1024 / 1024} MB)")

    // 압축 후 예상 크기 (AAC 128kbps 기준)
    val compressedBitsPerSecond = 128 * 1024
    val compressedBytes = (compressedBitsPerSecond / 8) * seconds
    val compressionRatio = totalBytes.toFloat() / compressedBytes

    println("\n압축 후 (AAC 128kbps):")
    println("- 압축 크기: $compressedBytes bytes (${compressedBytes / 1024} KB)")
    println("- 압축률: ${String.format("%.1f", compressionRatio)}배")
}

// ==========================================
// 예제 3: 일반적인 오디오 품질
// ==========================================

fun commonAudioQualities() {
    println("\n=== 일반적인 오디오 품질 ===\n")

    val qualities = listOf(
        Triple("전화 품질", Triple(8000, 16, 1)),
        Triple("낮은 품질", Triple(16000, 16, 1)),
        Triple("보통 품질", Triple(32000, 16, 2)),
        Triple("CD 품질", Triple(44100, 16, 2)),
        Triple("스튜디오 품질", Triple(48000, 24, 2)),
        Triple("하이레즈", Triple(96000, 24, 2))
    )

    for ((name, config) in qualities) {
        val (sampleRate, bitDepth, channels) = config
        val bitsPerSecond = sampleRate * bitDepth * channels
        val kbps = bitsPerSecond / 1024

        val channelName = if (channels == 1) "모노" else "스테레오"
        println("$name: ${sampleRate}Hz, ${bitDepth}bit, $channelName → ${kbps} Kbps")
    }
}

// ==========================================
// 예제 4: PCM 샘플 생성 (사인파)
// ==========================================

fun generateSineWave(frequency: Double, sampleRate: Int, duration: Double): ByteArray {
    val samples = (sampleRate * duration).toInt()
    val buffer = ByteArray(samples * 2) // 16비트 = 2바이트

    for (i in 0 until samples) {
        // 사인파 생성: amplitude * sin(2π * frequency * time)
        val time = i.toDouble() / sampleRate
        val value = (32767 * 0.5 * sin(2 * PI * frequency * time)).toInt().toShort()

        // 16비트 리틀 엔디안으로 저장
        buffer[i * 2] = (value.toInt() and 0xFF).toByte()         // 하위 바이트
        buffer[i * 2 + 1] = ((value.toInt() shr 8) and 0xFF).toByte()  // 상위 바이트
    }

    return buffer
}

fun demonstrateSineWave() {
    println("\n=== PCM 샘플 생성 (440Hz 사인파) ===")

    val sampleRate = 44100
    val frequency = 440.0  // A4 음표
    val duration = 0.01    // 10ms

    val pcmData = generateSineWave(frequency, sampleRate, duration)

    println("생성된 PCM 데이터:")
    println("- 주파수: 440Hz (A4 음표)")
    println("- 샘플레이트: $sampleRate Hz")
    println("- 길이: $duration 초")
    println("- 데이터 크기: ${pcmData.size} bytes")
    println("")

    // 첫 10개 샘플 출력
    println("첫 10개 샘플 (16비트 값):")
    for (i in 0 until min(10, pcmData.size / 2)) {
        val low = pcmData[i * 2].toInt() and 0xFF
        val high = pcmData[i * 2 + 1].toInt() and 0xFF
        val value = (high shl 8) or low
        val signedValue = value.toShort()
        println("  샘플 $i: $signedValue")
    }
}

// ==========================================
// 예제 5: 스테레오 PCM 데이터 구조
// ==========================================

fun demonstrateStereoLayout() {
    println("\n=== 스테레오 PCM 데이터 구조 ===\n")

    println("모노 (1채널):")
    println("┌────────┬────────┬────────┬────────┐")
    println("│ 샘플1  │ 샘플2  │ 샘플3  │ 샘플4  │")
    println("└────────┴────────┴────────┴────────┘")
    println("각 샘플: 2바이트 (16비트)\n")

    println("스테레오 (2채널):")
    println("┌────────┬────────┬────────┬────────┬────────┬────────┐")
    println("│   L1   │   R1   │   L2   │   R2   │   L3   │   R3   │")
    println("└────────┴────────┴────────┴────────┴────────┴────────┘")
    println("L = 왼쪽 채널, R = 오른쪽 채널")
    println("각 샘플: 2바이트, 총 4바이트/프레임")
}

// ==========================================
// 예제 6: PCM 버퍼 읽기 패턴
// ==========================================

fun readPCMStereoSample(pcmBuffer: ByteArray, sampleIndex: Int): Pair<Short, Short> {
    // 16비트 스테레오 PCM
    // 각 샘플 = 4바이트 (2바이트 L + 2바이트 R)

    val offset = sampleIndex * 4

    // 왼쪽 채널 (리틀 엔디안)
    val leftLow = pcmBuffer[offset].toInt() and 0xFF
    val leftHigh = pcmBuffer[offset + 1].toInt() and 0xFF
    val leftValue = ((leftHigh shl 8) or leftLow).toShort()

    // 오른쪽 채널
    val rightLow = pcmBuffer[offset + 2].toInt() and 0xFF
    val rightHigh = pcmBuffer[offset + 3].toInt() and 0xFF
    val rightValue = ((rightHigh shl 8) or rightLow).toShort()

    return Pair(leftValue, rightValue)
}

// ==========================================
// 예제 7: 샘플레이트 변환 (리샘플링)
// ==========================================

fun demonstrateResampling() {
    println("\n=== 샘플레이트 변환 (리샘플링) ===\n")

    println("시나리오: 44100Hz → 16000Hz 변환")
    println("")
    println("왜 필요한가?")
    println("- 전화 통화: 8kHz ~ 16kHz")
    println("- 음성 인식: 16kHz")
    println("- 데이터 절약: 낮은 샘플레이트 = 작은 크기")
    println("")

    val sourceSampleRate = 44100
    val targetSampleRate = 16000
    val ratio = targetSampleRate.toFloat() / sourceSampleRate

    println("변환 비율: $ratio")
    println("예상 크기 감소: ${(1 - ratio) * 100}%")
    println("")
    println("⚠️  주의: 샘플레이트를 낮추면 고주파 정보가 손실됩니다!")
    println("   (나이퀴스트 정리: 샘플레이트의 절반까지만 재현 가능)")
}

// ==========================================
// 예제 8: RootEncoder에서의 PCM 사용
// ==========================================

fun rootEncoderPCMFlow() {
    println("\n=== RootEncoder PCM 흐름 ===\n")

    println("1️⃣  MicrophoneManager")
    println("   - AudioRecord로 마이크에서 PCM 캡처")
    println("   - byte[] pcmBuffer = new byte[8192]")
    println("   - 설정: 32000Hz, 16bit, 스테레오")
    println("   ↓")

    println("2️⃣  AudioPostProcessEffect (선택)")
    println("   - 에코 제거 (Echo Cancellation)")
    println("   - 노이즈 억제 (Noise Suppression)")
    println("   ↓")

    println("3️⃣  AudioEncoder")
    println("   - PCM → AAC 압축")
    println("   - MediaCodec 사용")
    println("   - 입력: PCM 16비트")
    println("   - 출력: AAC 64kbps")
    println("   ↓")

    println("4️⃣  RtmpClient")
    println("   - AAC → FLV 오디오 패킷")
    println("   - RTMP 서버로 전송")

    println("\n압축 효과:")
    val pcmBitrate = 32000 * 16 * 2  // 1024 kbps
    val aacBitrate = 64 * 1024       // 64 kbps
    val compression = pcmBitrate.toFloat() / aacBitrate
    println("PCM: ${pcmBitrate / 1024} Kbps")
    println("AAC: ${aacBitrate / 1024} Kbps")
    println("압축률: ${String.format("%.1f", compression)}배")
}

// ==========================================
// 메인 함수
// ==========================================

fun main() {
    understandPCM()

    // 예제 2: 크기 계산
    calculatePCMSize(44100, 16, 2, 60)  // 1분 CD 품질

    // 예제 3: 일반적인 품질
    commonAudioQualities()

    // 예제 4: 사인파 생성
    demonstrateSineWave()

    // 예제 5: 스테레오 레이아웃
    demonstrateStereoLayout()

    // 예제 7: 리샘플링
    demonstrateResampling()

    // 예제 8: RootEncoder 흐름
    rootEncoderPCMFlow()

    println("\n" + "=".repeat(50))
    println("PCM 오디오 학습 완료! 🎵")
    println("=".repeat(50))
}

/**
 * 퀴즈:
 *
 * Q1. 44100Hz, 16bit, 스테레오 PCM 데이터 1초는 몇 바이트?
 * A: 44100 × 2 (16bit) × 2 (스테레오) = 176,400 bytes
 *
 * Q2. 왜 스트리밍에서 PCM을 그대로 전송하지 않나요?
 * A: 데이터가 너무 큽니다! AAC로 압축하면 10분의 1 크기로 줄어듭니다.
 *
 * Q3. 16비트 PCM에서 무음은 어떤 값?
 * A: 0 (정확히는 0x0000). 범위는 -32768 ~ 32767입니다.
 *
 * Q4. 모노와 스테레오의 차이는?
 * A: 모노는 1채널 (모든 스피커에서 같은 소리)
 *    스테레오는 2채널 (왼쪽/오른쪽 다른 소리 → 공간감)
 *
 * Q5. 나이퀴스트 주파수(Nyquist Frequency)란?
 * A: 샘플레이트의 절반. 44100Hz로 샘플링하면 최대 22050Hz까지 재현 가능.
 *    (인간의 가청 주파수: 20Hz ~ 20000Hz)
 */
