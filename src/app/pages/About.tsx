import { Heart, Target, Users, TrendingUp } from "lucide-react";

export function About() {
  return (
    <div className="px-4 pt-5 pb-4">
      {/* Hero */}
      <div className="bg-gradient-to-r from-purple-600 to-pink-500 rounded-2xl p-5 mb-5 text-white">
        <p className="text-purple-200 text-xs mb-1" style={{ fontWeight: 500 }}>
          Sobre o projeto
        </p>
        <h1 className="text-xl mb-2" style={{ fontWeight: 700 }}>
          Afirma<span className="text-pink-200">+</span>
        </h1>
        <p className="text-sm text-purple-100 leading-relaxed">
          Conectando talento e oportunidade, promovendo diversidade e inclusão no mercado de trabalho.
        </p>
      </div>

      {/* Mission */}
      <div className="bg-white rounded-2xl p-4 shadow-sm mb-3">
        <div className="flex items-center gap-3 mb-3">
          <div className="bg-purple-100 p-2.5 rounded-xl">
            <Target className="w-5 h-5 text-purple-600" />
          </div>
          <h2 className="text-slate-800" style={{ fontWeight: 700, fontSize: "15px" }}>
            Nossa Missão
          </h2>
        </div>
        <p className="text-sm text-slate-600 leading-relaxed">
          Criar pontes entre empresas comprometidas com a diversidade e profissionais
          talentosos que historicamente enfrentam barreiras no mercado de trabalho.
          Acreditamos que a diversidade é um motor de inovação e crescimento.
        </p>
      </div>

      {/* Values */}
      <div className="grid grid-cols-1 gap-3 mb-3">
        {[
          {
            icon: Heart,
            color: "bg-pink-100",
            iconColor: "text-pink-600",
            title: "Inclusão",
            desc: "Promovemos ambientes onde todas as pessoas se sintam valorizadas, independente de sua origem.",
          },
          {
            icon: Users,
            color: "bg-purple-100",
            iconColor: "text-purple-600",
            title: "Equidade",
            desc: "Trabalhamos para garantir oportunidades justas para grupos historicamente marginalizados.",
          },
          {
            icon: TrendingUp,
            color: "bg-indigo-100",
            iconColor: "text-indigo-600",
            title: "Crescimento",
            desc: "Conectamos pessoas a oportunidades que impulsionam suas carreiras e desenvolvimento.",
          },
        ].map((item) => {
          const Icon = item.icon;
          return (
            <div key={item.title} className="bg-white rounded-2xl p-4 shadow-sm flex items-start gap-3">
              <div className={`${item.color} p-2.5 rounded-xl shrink-0`}>
                <Icon className={`w-5 h-5 ${item.iconColor}`} />
              </div>
              <div>
                <p className="text-slate-800 mb-1" style={{ fontWeight: 600, fontSize: "14px" }}>
                  {item.title}
                </p>
                <p className="text-sm text-slate-500 leading-relaxed">{item.desc}</p>
              </div>
            </div>
          );
        })}
      </div>

      {/* Impact Stats */}
      <div className="bg-gradient-to-r from-purple-600 to-pink-500 rounded-2xl p-5 mb-3 text-white">
        <h2 className="text-center mb-4" style={{ fontWeight: 700, fontSize: "15px" }}>
          Nosso Impacto
        </h2>
        <div className="grid grid-cols-3 gap-2 text-center">
          {[
            { value: "500+", label: "Vagas publicadas" },
            { value: "150+", label: "Empresas parceiras" },
            { value: "2k+", label: "Cadastradas(os)" },
          ].map((stat) => (
            <div key={stat.label}>
              <p className="text-2xl text-white" style={{ fontWeight: 700 }}>
                {stat.value}
              </p>
              <p className="text-[11px] text-purple-200 mt-0.5">{stat.label}</p>
            </div>
          ))}
        </div>
      </div>

      {/* For who */}
      <div className="bg-white rounded-2xl p-4 shadow-sm mb-3">
        <h2 className="text-slate-800 mb-3" style={{ fontWeight: 700, fontSize: "15px" }}>
          Para quem é o Afirma+
        </h2>
        <div className="space-y-3">
          <div className="border-l-4 border-purple-500 pl-3">
            <p className="text-sm text-slate-800 mb-1" style={{ fontWeight: 600 }}>
              👩🏽‍💼 Mulheres
            </p>
            <p className="text-sm text-slate-500 leading-relaxed">
              Especialmente mulheres negras, que enfrentam dupla discriminação. Conectamos com empresas
              que valorizam e respeitam seu potencial.
            </p>
          </div>
          <div className="border-l-4 border-pink-500 pl-3">
            <p className="text-sm text-slate-800 mb-1" style={{ fontWeight: 600 }}>
              🧑🏾‍💻 Pessoas Pretas
            </p>
            <p className="text-sm text-slate-500 leading-relaxed">
              Conectamos profissionais negros a empresas comprometidas com diversidade racial
              em todos os níveis hierárquicos.
            </p>
          </div>
        </div>
      </div>

      {/* CTA */}
      <div className="bg-slate-100 rounded-2xl p-5 text-center">
        <h2 className="text-slate-800 mb-2" style={{ fontWeight: 700, fontSize: "15px" }}>
          Junte-se a Nós
        </h2>
        <p className="text-sm text-slate-500 mb-4 leading-relaxed">
          Empresa ou profissional — juntos construímos um mercado mais justo e inclusivo.
        </p>
        <a
          href="mailto:contato@afirmamais.com"
          className="inline-block bg-gradient-to-r from-purple-600 to-pink-500 text-white px-6 py-3 rounded-xl text-sm"
          style={{ fontWeight: 600 }}
        >
          Entre em contato
        </a>
      </div>
    </div>
  );
}
